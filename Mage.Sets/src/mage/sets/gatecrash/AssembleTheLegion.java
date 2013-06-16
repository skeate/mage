/*
*  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
*
*  Redistribution and use in source and binary forms, with or without modification, are
*  permitted provided that the following conditions are met:
*
*     1. Redistributions of source code must retain the above copyright notice, this list of
*        conditions and the following disclaimer.
*
*     2. Redistributions in binary form must reproduce the above copyright notice, this list
*        of conditions and the following disclaimer in the documentation and/or other materials
*        provided with the distribution.
*
*  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
*  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
*  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
*  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
*  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
*  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
*  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
*  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
*  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
*  The views and conclusions contained in the software and documentation are those of the
*  authors and should not be interpreted as representing official policies, either expressed
*  or implied, of BetaSteward_at_googlemail.com.
*/
package mage.sets.gatecrash;

import java.util.UUID;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.cards.CardImpl;
import mage.counters.Counter;
import mage.game.Game;
import mage.game.permanent.Permanent;

/**
*
* @author LevelX2
*/
public class AssembleTheLegion extends CardImpl<AssembleTheLegion> {

    public AssembleTheLegion(UUID ownerId) {
       super(ownerId, 142, "Assemble the Legion", Rarity.RARE, new CardType[]{CardType.ENCHANTMENT}, "{3}{R}{W}");
       this.expansionSetCode = "GTC";

       this.color.setRed(true);
       this.color.setWhite(true);

       // At the beginning of your upkeep, put a muster counter on Assemble the Legion. Then put a 1/1 red and white Soldier creature token with haste onto the battlefield for each muster counter on Assemble the Legion.
       this.addAbility(new BeginningOfUpkeepTriggeredAbility(new AssembleTheLegionEffect(), TargetController.YOU, false));
    }

    public AssembleTheLegion(final AssembleTheLegion card) {
       super(card);
    }

    @Override
    public AssembleTheLegion copy() {
       return new AssembleTheLegion(this);
    }
}

class AssembleTheLegionEffect extends OneShotEffect<AssembleTheLegionEffect> {

    public AssembleTheLegionEffect() {
       super(Outcome.Copy);
       this.staticText = "put a muster counter on {this}. Then put a 1/1 red and white Soldier creature token with haste onto the battlefield for each muster counter on {this}";
    }

    public AssembleTheLegionEffect(final AssembleTheLegionEffect effect) {
       super(effect);
    }

    @Override
    public AssembleTheLegionEffect copy() {
       return new AssembleTheLegionEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
       /* 1/24/2013: If Assemble the Legion isn’t on the battlefield when its ability resolves,
        * use the number of muster counters it had when it was last on the battlefield to
        * determine how many Soldier tokens to put onto the battlefield.
        */

       Permanent sourcePermanent = game.getPermanent(source.getSourceId());
       int amountCounters = 0;
       if (sourcePermanent == null) {
           Permanent lki = (Permanent) game.getLastKnownInformation(source.getSourceId(), Zone.BATTLEFIELD);
           if (lki != null) {
               amountCounters = lki.getCounters().getCount("Muster");
           }
       } else {
           new AddCountersSourceEffect(new MusterCounter(),false).apply(game, source);
           amountCounters = sourcePermanent.getCounters().getCount("Muster");
           
       }
       if (amountCounters > 0) {
           return new CreateTokenEffect(new SoldierToken(), amountCounters).apply(game, source);
       }
       return false;
    }
}

class MusterCounter extends Counter<MusterCounter> {

    public MusterCounter() {
       super("Muster");
       this.count = 1;
    }

    public MusterCounter(int amount) {
       super("Muster");
       this.count = amount;
    }
}